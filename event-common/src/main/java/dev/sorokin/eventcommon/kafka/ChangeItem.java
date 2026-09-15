package dev.sorokin.eventcommon.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeItem {

    private String field;

    private Object oldValue;

    private Object newValue;
}
