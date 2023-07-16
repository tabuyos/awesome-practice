/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.dysql.quickstart;
/**
 * DysqlMain
 *
 * @author tabuyos
 * @since 2023/5/31
 */
@SuppressWarnings("DataFlowIssue")
public class DysqlMain {

  public static void main(String[] args){
    SqlBuilder sb = null;

    sb.select(ctx -> {
      ctx.col("name");
      ctx.fun("length(code)");
    }).from(ctx -> {
      ctx.table("course");
      ctx.join("student", "course.sid = student.id");
      ctx.leftJoin("", "");
      ctx.rightJoin("", "");
    }).where(ctx -> {
      ctx.cond("student.score = ?", 12);
      ctx.and(() -> {
        // and
        ctx.cond("course.code in (?, ?)", "A1", "B2");
      });
      ctx.or(() -> {
        // or
        ctx.cond("course.code in (?, ?)", "A1", "B2");
      });
    });
  }
}
