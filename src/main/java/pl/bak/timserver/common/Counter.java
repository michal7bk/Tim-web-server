package pl.bak.timserver.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Counter {

    String count;

    public Counter(Long longValue) {
        this.count = String.valueOf(longValue);
    }
}
