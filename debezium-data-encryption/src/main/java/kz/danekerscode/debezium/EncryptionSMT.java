package kz.danekerscode.debezium;

import kz.danekerscode.debezium.singletons.SmtTextEncryptor;
import kz.danekerscode.debezium.utils.FieldListHolder;
import kz.danekerscode.debezium.utils.FieldNameHelper;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.transforms.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EncryptionSMT<R extends ConnectRecord<R>> implements Transformation<R> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private FieldListHolder fields;

    @Override
    public R apply(R record) {
        Struct value = (Struct) record.value();
        if (value != null) {
            var updatedValue = processBothStructs(value);

            return record.newRecord(
                    record.topic(),
                    record.kafkaPartition(),
                    record.keySchema(),
                    record.key(),
                    updatedValue.schema(),
                    updatedValue,
                    record.timestamp()
            );
        } else {
            throw new DataException("Record value is null");
        }
    }

    @Override
    public ConfigDef config() {
        return new ConfigDef()
                .define(
                        SmtConstants.FIELDS, ConfigDef.Type.STRING,
                        ConfigDef.Importance.HIGH, "Comma-separated list of schemaName.tableName.columnName field names to encrypt"
                );
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
        log.info("Received map for configure method: {}", configs);
        var fieldNamesConfig = (String) configs.get(SmtConstants.FIELDS);
        this.fields = new FieldListHolder(fieldNamesConfig);
        log.info("Configured to encrypt fields: {}", fields);
    }

    private Struct processBothStructs(Struct valueStruct) {
        var before = valueStruct.getStruct(SmtConstants.BEFORE);
        var after = valueStruct.getStruct(SmtConstants.AFTER);

        if (before != null || after != null) {
            Struct updatedBefore = before != null ? processStruct(before, valueStruct) : null;
            Struct updatedAfter = after != null ? processStruct(after, valueStruct) : null;

            var updatedValue = new Struct(valueStruct.schema())
                    .put(SmtConstants.BEFORE, updatedBefore)
                    .put(SmtConstants.AFTER, updatedAfter);

            for (var field : valueStruct.schema().fields()) {
                if (!field.name().equals(SmtConstants.BEFORE) && !field.name().equals(SmtConstants.AFTER)) {
                    updatedValue.put(field.name(), valueStruct.get(field));
                }
            }

            return updatedValue;
        }

        return valueStruct;
    }

    private Struct processStruct(Struct struct, Struct valueStruct) {
        var updatedStruct = new Struct(struct.schema());

        var schemaName = valueStruct.getStruct(SmtConstants.SOURCE).getString(SmtConstants.SCHEMA);
        var tableName = valueStruct.getStruct(SmtConstants.SOURCE).getString(SmtConstants.TABLE);

        for (var field : struct.schema().fields()) {
            var fullFieldName = FieldNameHelper.constructFieldPath(schemaName, tableName, field.name());
            if (fields.exists(fullFieldName)) {

                String originalValue = struct.getString(field.name());
                String encryptedValue;

                encryptedValue = SmtTextEncryptor.encrypt(originalValue);
                log.info("Encrypted field: {} -> {}", fullFieldName, encryptedValue);
                updatedStruct.put(field.name(), encryptedValue);
            } else {
                updatedStruct.put(field.name(), struct.get(field));
            }
        }

        return updatedStruct;
    }

}