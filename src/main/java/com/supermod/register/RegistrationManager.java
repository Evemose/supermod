package com.supermod.register;

import lombok.NonNull;

public interface RegistrationManager extends Comparable<RegistrationManager> {

    void registerAllOnClasspath();

    @Override
    default int compareTo(@NonNull RegistrationManager other) {
        return Long.compare(priority(), other.priority());
    }

    default long priority() {
        return 0;
    }
}
