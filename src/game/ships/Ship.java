package game.ships;

import game.engine.Point;

import javax.xml.bind.annotation.*;


@XmlType(propOrder = {"shipId", "direction", "location"})
public class Ship {

    @XmlElement(name = "shipTypeId")
    private String shipId;

    @XmlElement(name = "direction")
    private String direction;

    @XmlElement(name = "position")
    private Point[] location;

}
