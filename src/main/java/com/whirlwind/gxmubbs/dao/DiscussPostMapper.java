package com.whirlwind.gxmubbs.dao;

import com.whirlwind.gxmubbs.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {
    /**
     *
     * @param userId 在个人首页能用到
     * @param offset  帖子编号起始
     * @param limit    一页能有多少帖子
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId,int offset,int limit);

    //查询有多少帖子
    //Param注解用于给参数取别名,
    //如果只有一个参数,并且在<if里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

}
