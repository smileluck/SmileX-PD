package top.zsmile.flink.cdc;

import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.MultipleParameterTool;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackendFactory;
import org.apache.flink.runtime.jobgraph.SavepointConfigOptions;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Properties;

public class MySqlBinlogSourceExample {

    public static void main(String[] args) throws Exception {
        MultipleParameterTool params = MultipleParameterTool.fromArgs(args);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setGlobalJobParameters(params);

//        Properties prop = new Properties();
        //initial，latest-offset
//        prop.setProperty("scan.startup.mode", "latest-offset");
//        prop.setProperty("debezium.snapshot.mode", "never");
//        prop.setProperty("state.checkpoints.dir", "D:\\dev-tools\\flink\\flink-checkpoint");
//        prop.setProperty(SavepointConfigOptions.SAVEPOINT_PATH.key(), "D:\\dev-tools\\flink\\flink-checkpoint");
//        prop.setProperty(CheckpointConfig.SAVEPOINT_PATH.key(), "D:\\dev-tools\\flink\\flink-checkpoint");
        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
                .hostname("zhyc.rwlb.rds.aliyuncs.com")
                .port(3306)
                .databaseList("heitan_db") // set captured database
                .tableList("heitan_db.tb_goods_category_log") // set captured table
                .username("datawarehouse")
                .password("Xiaoheitan@2022")
//                .debeziumProperties(prop)
                .deserializer(new JsonDebeziumDeserializationSchema()) // converts SourceRecord to JSON String
                .build();

        // 启用检查点，指定触发checkpoint的时间间隔（单位：毫秒，默认500毫秒），默认情况是不开启的
        env.enableCheckpointing(3000);

        env.getCheckpointConfig().setCheckpointStorage("file:/D:\\dev-tools\\flink\\flink-checkpoint");
//
//        // 设定语义模式，默认情况是exactly_once
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        // 设定Checkpoint超时时间，默认为10分钟
        env.getCheckpointConfig().setCheckpointTimeout(60000);
//        // 设定两个Checkpoint之间的最小时间间隔，防止出现例如状态数据过大而导致Checkpoint执行时间过长，从而导致Checkpoint积压过多，
//        // 最终Flink应用密切触发Checkpoint操作，会占用了大量计算资源而影响到整个应用的性能（单位：毫秒）
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(500);
//
//        // 默认情况下，只有一个检查点可以运行
//        // 根据用户指定的数量可以同时触发多个Checkpoint，进而提升Checkpoint整体的效率
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//        // 不会在任务正常停止的过程中清理掉检查点数据，而是会一直保存在外部系统介质中，另外也可以通过从外部检查点中对任务进行恢复
        env.getCheckpointConfig().setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//
//        // 重启3次，每次失败后等待10000毫秒
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3, 2000L));


//        env.setDefaultSavepointDirectory("D:\\dev-tools\\flink\\flink-checkpoint");
//        env.setStateBackend(new RocksDBStateBackend("file:/D:/dev-tools/flink/flink-checkpoint"));

        DataStreamSource<String> source = env
                .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "MySQL Source");
        
        // set 4 parallel source tasks
        source.setParallelism(1)
                .print(); // use parallelism 1 for sink to keep message ordering

        env.execute("Print MySQL Snapshot + Binlog");
    }
}

