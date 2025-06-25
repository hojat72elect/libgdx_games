

package com.shatteredpixel.shatteredpixeldungeon.levels.builders;

import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.Room;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;

//A simple builder which utilizes a line as its core feature.
public class LineBuilder extends RegularBuilder {

    @Override
    public ArrayList<Room> build(ArrayList<Room> rooms) {

        setupRooms(rooms);

        if (entrance == null) {
            return null;
        }

        float direction = Random.Float(0, 360);
        ArrayList<Room> branchable = new ArrayList<>();

        entrance.setSize();
        entrance.setPos(0, 0);

        mainPathRooms.add(0, entrance);
        if (exit != null) mainPathRooms.add(exit);

        if (shop != null) {
            placeRoom(rooms, entrance, shop, direction + 180f);
        }

        Room prev = entrance;

        float[] pathTunnels = pathTunnelChances.clone();
        for (int i = 1; i < mainPathRooms.size(); i++) {
            Room r = mainPathRooms.get(i);

            int tunnels = Random.chances(pathTunnels);
            if (tunnels == -1) {
                pathTunnels = pathTunnelChances.clone();
                tunnels = Random.chances(pathTunnels);
            }
            pathTunnels[tunnels]--;

            for (int j = 0; j < tunnels; j++) {
                ConnectionRoom t = ConnectionRoom.createRoom();
                placeRoom(rooms, prev, t, direction + Random.Float(-pathVariance, pathVariance));
                branchable.add(t);
                rooms.add(t);
                prev = t;
            }

            placeRoom(rooms, prev, r, direction + Random.Float(-pathVariance, pathVariance));
            branchable.add(r);
            prev = r;
        }

        ArrayList<Room> roomsToBranch = new ArrayList<>();
        roomsToBranch.addAll(multiConnections);
        roomsToBranch.addAll(singleConnections);
        weightRooms(branchable);
        if (!createBranches(rooms, branchable, roomsToBranch, branchTunnelChances)) {
            return null;
        }

        findNeighbours(rooms);

        for (Room r : rooms) {
            for (Room n : r.neigbours) {
                if (!n.connected.containsKey(r)
                        && Random.Float() < extraConnectionChance) {
                    r.connect(n);
                }
            }
        }

        return rooms;
    }
}
