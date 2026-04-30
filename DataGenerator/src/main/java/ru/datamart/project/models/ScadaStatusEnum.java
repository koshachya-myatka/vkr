package ru.datamart.project.models;

public enum ScadaStatusEnum {
    NORMAL {
        public String toString() {
            return "В НОРМЕ";
        }
    },
    WARNING {
        public String toString() {
            return "ПРЕДУПРЕЖДЕНИЕ";
        }
    },
    ALARM {
        public String toString() {
            return "ТРЕВОГА";
        }
    }
}
