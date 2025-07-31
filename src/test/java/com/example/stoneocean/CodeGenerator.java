package com.example.stoneocean;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.sql.Types;
import java.util.Collections;

public class CodeGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://debian-n5105.home:3306/stone_ocean?userUnicode=true&characterEncoding=utf-8&userSSL=false&serverTimezone=Asia/Shanghai&remarks=true&useInformationSchema=true",
                        "root",
                        "12321")
                .globalConfig(builder -> {
                    builder.author("warmstone") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(".\\src\\main\\java"); // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("com.example.stoneocean") // 设置父包名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, ".\\src\\main\\resources\\mapper")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder
                                .addInclude("t_user") // 设置需要生成的表名
                                .addInclude("t_vote4fun_rank_list") // 设置需要生成的表名
                                .addInclude("t_vote4fun_rank_member") // 设置需要生成的表名
                                .addInclude("t_vote4fun_announcement") // 设置需要生成的表名
                                .addTablePrefix("t_vote4fun_", "t_") // 设置过滤表前缀
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
