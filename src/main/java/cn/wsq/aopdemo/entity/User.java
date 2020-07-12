package cn.wsq.aopdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Silent
 * @date 2020-7-12 11:51:34
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String email;
}
