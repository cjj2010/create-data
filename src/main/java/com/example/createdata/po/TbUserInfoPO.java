package com.example.createdata.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author CJJ
 * @version 1.0
 * @description: TODO
 * @date 2023/1/16
 */


@Data
@TableName("tb_user_info_500000")
public class TbUserInfoPO {



    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String tno;
    private String name;
    private String name1;
    private char gender;
    private String password;
    private String email;
    private String telephone;
    private String address;
    private String address1;
    private String portraitPath;
    private String carNumber;
    private String ipv4;
    private String appInfo;
    private Date createTime;
    private Date updateTime;
    private BigDecimal monetary;
    private BigDecimal monetary1;
    private BigDecimal monetary2;
    private BigDecimal monetary3;





}
