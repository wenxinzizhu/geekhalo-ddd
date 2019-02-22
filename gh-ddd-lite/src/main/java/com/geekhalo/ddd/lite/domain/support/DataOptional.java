package com.geekhalo.ddd.lite.domain.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiModel(
        value = "DataOptional",
        description = "数据包装器"
)
public final class DataOptional<T> {
    @ApiModelProperty("值")
    private T value;

    private DataOptional() {
        this.value = null;
    }

    private DataOptional(T value) {
        this.value = value;
    }

    public static <T> DataOptional<T> of(T value) {
        return new DataOptional(value);
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        consumer.accept(this.value);
    }

    public T orElse(T other) {
        return this.value != null ? this.value : other;
    }

    public T orElseGet(Supplier<? extends T> other) {
        return this.value != null ? this.value : other.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.value != null) {
            return this.value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof DataOptional)) {
            return false;
        } else {
            DataOptional<?> other = (DataOptional)obj;
            return Objects.equals(this.value, other.value);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    @Override
    public String toString() {
        return this.value != null ? String.format("DataOptional[%s]", this.value) : "DataOptional.empty";
    }
}
