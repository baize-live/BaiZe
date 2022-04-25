package top.byzeyou.mapper;

import top.byzeyou.bean.Game;

import java.util.List;

public interface GameUnloadListMapper {
    // 获得全部游戏地址
    List<Game> selectAll();

    // 添加游戏
    void addGame(Game game);
}
