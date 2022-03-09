package top.zsmile.flink.config;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ReadableConfig;

import java.util.Optional;

public class PathReadableConfig implements ReadableConfig {
    @Override
    public <T> T get(ConfigOption<T> configOption) {
        return configOption.defaultValue();
    }

    @Override
    public <T> Optional<T> getOptional(ConfigOption<T> configOption) {
        return Optional.empty();
    }
}
