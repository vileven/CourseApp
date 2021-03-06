package api.utils.validator;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;


@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum ValidatorStatus {
    ERROR("error"),
    WARNING("warning"),
    OK("ok")
    ;


    private final String text;


    ValidatorStatus(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    @JsonValue
    public String getValue() {
        return text;
    }
}
