/*
 * This file is part of the UG-Java Core package.
 *
 * (c) Ulrich Geraud AHOGLA. <iamcleancoder@gmail.com>
 */

/*
 * @author Ulrich Geraud AHOGLA. <iamcleancoder@gmail.com
 */

package com.ug.enums;

public enum Status {
    SUCCESS("success"),
    ERROR("error");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value.toLowerCase();
    }
}