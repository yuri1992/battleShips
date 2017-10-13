
package descriptor;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the descriptor package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: descriptor
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BattleShipGame }
     * 
     */
    public BattleShipGame createBattleShipGame() {
        return new BattleShipGame();
    }

    /**
     * Create an instance of {@link Boards }
     * 
     */
    public Boards createBattleShipGameBoards() {
        return new Boards();
    }

    /**
     * Create an instance of {@link Board }
     * 
     */
    public Board createBattleShipGameBoardsBoard() {
        return new Board();
    }

    /**
     * Create an instance of {@link Ship }
     * 
     */
    public Ship createBattleShipGameBoardsBoardShip() {
        return new Ship();
    }

    /**
     * Create an instance of {@link ShipTypes }
     * 
     */
    public ShipTypes createBattleShipGameShipTypes() {
        return new ShipTypes();
    }

    /**
     * Create an instance of {@link Mine }
     * 
     */
    public Mine createBattleShipGameMine() {
        return new Mine();
    }

    /**
     * Create an instance of {@link Position }
     * 
     */
    public Position createBattleShipGameBoardsBoardShipPosition() {
        return new Position();
    }

    /**
     * Create an instance of {@link ShipType }
     * 
     */
    public ShipType createBattleShipGameShipTypesShipType() {
        return new ShipType();
    }

}
