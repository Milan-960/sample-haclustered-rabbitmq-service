package pl.piomin.services.rabbit.commons.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = -5810415223475626164L;
    private Integer id;
    private String description;
    private OrderType type;
}
