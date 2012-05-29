package com.redwerk.likelabs.domain.model.company.exception;

public class CompanyLogoTooBigException extends RuntimeException {

    private final long actualSize;

    private final long maxAllowedSize;

    public CompanyLogoTooBigException(long actualSize, long maxAllowedSize) {
        this.actualSize = actualSize;
        this.maxAllowedSize = maxAllowedSize;
    }

    public long getActualSize() {
        return actualSize;
    }

    public long getMaxAllowedSize() {
        return maxAllowedSize;
    }

}
