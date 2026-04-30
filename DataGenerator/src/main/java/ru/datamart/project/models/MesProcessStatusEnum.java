package ru.datamart.project.models;

public enum MesProcessStatusEnum {
    ARRIVAL {
        public String toString() {
            return "ПОСТУПЛЕНИЕ";
        }
    },
    PROCESSING {
        public String toString() {
            return "ОБРАБОТКА";
        }
    },
    ANALYSIS {
        public String toString() {
            return "АНАЛИЗ";
        }
    },
    ACCEPTED {
        public String toString() {
            return "ОДОБРЕНО";
        }
    },
    DEFECTIVE {
        public String toString() {
            return "БРАК";
        }
    }
}