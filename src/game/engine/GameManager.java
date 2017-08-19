package game.engine;

import game.Player;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "BattleShipGame")
public class GameManager {

    @XmlElement(name = "GameType", required = true)
    private GameMode mode = GameMode.BASIC;

    @XmlElement(name = "boardSize", required = true)
    private int boardSize;

    @XmlElementWrapper(name = "shipTypes")
    protected GameManager.shipType shipType[];

    @XmlElementWrapper(name = "boards")
    @XmlElement(name = "board")
    protected Player playerList[];

    public boolean isRunning;

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "shipType")
    public static class shipType {
        @XmlAttribute(name = "id")
        private String id;

        @XmlElement(name = "category")
        private String category;

        @XmlElement(name = "amount")
        private int amount;

        @XmlElement(name = "length")
        private int length;

        @XmlElement(name = "score")
        private int score;
    }


    public GameManager() {
    }
}
