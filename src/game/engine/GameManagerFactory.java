package game.engine;

import descriptor.BattleShipGame;
import descriptor.Board;
import game.exceptions.*;
import game.players.Player;
import game.ships.Ship;
import game.ships.ShipType;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by amirshavit on 9/14/17.
 */
public class GameManagerFactory {

    public static final int MIN_BOARD_SIZE = 5;
    public static final int MAX_BOARD_SIZE = 20;

    static public GameManager loadGameManager(String fileName) throws FileNotFoundException,
            JAXBException, GameSettingsInitializationException {
        BattleShipGame gameDescriptor = JAXBGameParser.loadGameFromXML(fileName);
        GameManager game = parseXmlDescriptor(gameDescriptor);
        return game;
    }

    static public GameManager loadGameManager(File xml) throws FileNotFoundException, GameSettingsInitializationException, JAXBException {
        BattleShipGame gameDescriptor = JAXBGameParser.loadGameFromFile(xml);
        GameManager game = parseXmlDescriptor(gameDescriptor);
        return game;
    }

    static private GameManager parseXmlDescriptor(BattleShipGame gameDescriptor) throws GameSettingsInitializationException {
        validateGameMode(gameDescriptor.getGameType());
        GameMode mode = GameMode.valueOf(gameDescriptor.getGameType());

        int size = gameDescriptor.getBoardSize();
        validateBoardSize(size);

        List<ShipType> types = parseShipTypes(gameDescriptor.getShipTypes().getShipType());
        validateShipTypes(types, mode);
        List<Player> players = parsePlayers(gameDescriptor.getBoards().getBoard(), types, size);
        validateShipCount(types, players);

        return new GameManager(mode, size, types, players);
    }

    static private List<ShipType> parseShipTypes(List<descriptor.ShipType> descTypes) {
        List<ShipType> types = new ArrayList<>();
        for (descriptor.ShipType type : descTypes) {
            types.add(new ShipType(type.getId(), type.getCategory(), type.getAmount(), type.getLength(), type
                    .getScore()));
        }
        return types;
    }

    static private List<Player> parsePlayers(List<Board> playerList, List<ShipType> types, int boardSize) throws
            GameSettingsInitializationException {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerList.size(); ++i) {
            descriptor.Board descBoard = playerList.get(i);

            List<game.ships.Ship> ships = new ArrayList<>();

            for (descriptor.Ship item : descBoard.getShip()) {
                String shipTypeId = item.getShipTypeId();
                Optional<ShipType> type = types.stream().filter(tp -> tp.getTypeId().equals(shipTypeId)).findFirst();
                if (type.isPresent()) {
                    ships.add(new Ship(shipTypeId, item.getDirection(), item.getPosition(), type.get()));
                } else {
                    throw new GameSettingsInitializationException("Ship type " + shipTypeId + " not found");
                }
            }
            validateShipDirections(ships);

            players.add(new Player(ships, boardSize, i + 1));
        }
        return players;
    }


    static private void validateShipTypes(List<ShipType> types, GameMode mode) throws GameSettingsInitializationException {
        Map<String, Integer> shipTypeCount = new HashMap<>();

        for (ShipType st : types) {
            if (mode == GameMode.BASIC && st.getCategory().getGameMode() == GameMode.ADVANCE) {
                throw new GameSettingsInitializationException("Ship type " + st.getCategory() + " is not " +
                        "supported on " + mode + " game");
            }

            // Make sure each type is declared just once
            if (shipTypeCount.get(st.getTypeId()) == null) {
                shipTypeCount.put(st.getTypeId(), 1);
            } else {
                throw new DuplicatedShipTypesDecleared(st.getTypeId() + " Already Declared.");
            }
        }
    }

    static private void validateShipDirections(List<game.ships.Ship> ships) throws GameSettingsInitializationException {
        for (Ship ship : ships) {
            if (ship.getDirection().getCategory() != ship.getMeta().getCategory()) {
                throw new GameSettingsInitializationException("Ship direction configuration does not fit");
            }
        }
    }

    private static void validateShipCount(List<ShipType> types, List<Player> players) throws
            GameSettingsInitializationException {

        // Validating Each player have all necessary ships
        Map<String, Integer> shipTypeCount = new HashMap<>();
        for (ShipType type : types) {
            shipTypeCount.put(type.getTypeId(), type.getAmount());
        }

        for (Player p : players) {
            Map<String, Integer> playerShipTypeCount = new HashMap<>(shipTypeCount);
            for (Ship ship : p.getShips()) {
                Integer count = playerShipTypeCount.get(ship.getShipType());
                if (count == null)
                    throw new NotRecognizedShipType("Unrecognized ship type: " + ship.getShipType());
                else if (count == 0)
                    throw new NotEnoughShipsLocated(p.toString() + ": Declared " + ship.getShipType() + " too " +
                            "many times");
                playerShipTypeCount.put(ship.getShipType(), count - 1);
            }

            for (Map.Entry<String, Integer> e : playerShipTypeCount.entrySet()) {
                if (e.getValue() > 0) {
                    throw new NotEnoughShipsLocated(p.toString() + ": Missing " + e.getValue() + " ships of " +
                            e.getKey() + " type");
                }
            }
        }
    }

    private static void validateGameMode(String gameType) throws BoardBuilderException {
        try {
            GameMode.valueOf(gameType);
        } catch (IllegalArgumentException e) {
            throw new BoardBuilderException("Illegal game type provided");
        }
    }

    private static void validateBoardSize(int size) throws InvalidBoardSizeException {
        if (size > MAX_BOARD_SIZE || size < MIN_BOARD_SIZE) {
            throw new InvalidBoardSizeException("Board size must be between 5 to 20");
        }
    }
}
