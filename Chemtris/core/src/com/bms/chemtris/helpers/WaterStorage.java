package com.bms.chemtris.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.bms.chemtris.elements.Carbon;
import com.bms.chemtris.elements.Element;
import com.bms.chemtris.elements.Hydrogen;
import com.bms.chemtris.elements.Oxygen;

/**
 * Class to manage water storage.
 */
public class WaterStorage {
    public Array<Element> water_molecules;
    public Array<Element> hydrogens;
    public Array<Element> oxygens;
    public Array<Element> methane;
    private int frame;
    private int waters, stables;
    private int cc4;
    public boolean over;

    public WaterStorage(){
        //setup the elements array
        hydrogens = new Array<Element>();
        oxygens = new Array<Element>();
        water_molecules = new Array<Element>();
        methane = new Array<Element>();
        frame = 0;
        waters = 0;
        stables = 0;
        cc4 =0;
        over = false;
    }

    //method to add hydrogen
    public void add_hydrogen(Element h){
        hydrogens.add(h);
        h.fillBoard();
        Vector3 v = new Vector3();
        h.atom.transform.getTranslation(v);
        if(v.y >= 6f){
            over = true;
        }
    }

    //method to add carbon
    public void add_carbon(Element c){
        Vector3 v = new Vector3();
        c.atom.transform.getTranslation(v);

        Node n1 = c.atom.getNode(Carbon.TOP_VE_1);
        Node n2 = c.atom.getNode(Carbon.TOP_VE_2);
        Node n3 = c.atom.getNode(Carbon.BOTTOM_VE_1);
        Node n4 = c.atom.getNode(Carbon.BOTTOM_VE_2);

        if(v.y + n1.translation.y > 6f){
            over = true;
        }

        if(v.y + n2.translation.y > 6f){
            over = true;
        }

        if(v.y + n3.translation.y > 6f){
            over = true;
        }

        if(v.y + n4.translation.y > 6f){
            over = true;
        }

        Vector3 vf = new Vector3();

        c.atom.transform.getTranslation(vf);
        for (Node n : c.atom.nodes){
            int x = Math.round(vf.x + n.translation.x);
            int y = Math.round(vf.y + n.translation.y);
            int z = Math.round(vf.z + n.translation.z);

            for (Element hy : oxygens){
                Vector3 vh = new Vector3();
                hy.atom.transform.getTranslation(vh);
                int hsx = Math.round(vh.x);
                int hsy = Math.round(vh.y);
                int hsz = Math.round(vh.z);

                if(hsx == x &&  hsz == z){
                    if(hsy == y -1 || hsy == y+1){
                        hy.reduceValence();
                        for (Material m : hy.atom.materials) {
                            m.set(ColorAttribute.createDiffuse(new Color(204f / 255f, 102f / 255f, 0f / 255f, 1f)));
                        }
                        c.reduceValence();
                    }
                }
            }
        }

        c.fillBoard();
        hydrogens.add(c);
    }

    //method to add hydrogen for methane
    public boolean add_methane_h(Element h) {
        Element c = null;
        Vector3 v = new Vector3();
        h.atom.transform.getTranslation(v);
        oxygens.add(h);
        h.fillBoard();

        int xh = Math.round(v.x);
        int yh = Math.round(v.y);
        int zh = Math.round(v.z);


        if (yh > 6) {
            over = false;
        }

        for (Element ca : hydrogens) {
            Vector3 vc = new Vector3();
            ca.atom.transform.getTranslation(vc);

            for (Node n : ca.atom.nodes) {
                int x = Math.round(vc.x + n.translation.x);
                int y = Math.round(vc.y + n.translation.y);
                int z = Math.round(vc.z + n.translation.z);


                if (xh == x && zh == z) {
                    if (yh == y - 1 || yh == y+1) {
                        h.reduceValence();
                        for (Material m : h.atom.materials) {
                            m.set(ColorAttribute.createDiffuse(new Color(204f / 255f, 102f / 255f, 0f / 255f, 1f)));
                        }
                        ca.reduceValence();
                    }
                }
            }

            if(ca.isStable()){
                c = ca;
            }

        }



        if (c != null) {
            Vector3 vf = new Vector3();
            c.atom.transform.getTranslation(vf);

            for (Element hyd : oxygens) {
                int xc = Math.round(v.x);
                int yc = Math.round(v.y);
                int zc = Math.round(v.z);

                for(Node n : c.atom.nodes){
                    int nx = Math.round(v.x+n.translation.x);
                    int ny = Math.round(v.y+n.translation.y);
                    int nz = Math.round(v.z+n.translation.z);

                    if(nx == xc && nz == zc){
                        if(ny == yc - 1 || ny == yc+1){
                            water_molecules.add(hyd);
                            ++stables;
                        }
                    }
                }
            }

            water_molecules.add(c);
            for (Element e : water_molecules) {
                for (Material m : e.atom.materials) {
                    m.set(ColorAttribute.createDiffuse(new Color(204f / 255f, 102f / 255f, 0f / 255f, 1f)));
                }
            }
        }

        if (water_molecules.size > 4) {
            for (Element oxo : oxygens) {
                oxo.unfillBoard();
            }

            for (Element hydro : hydrogens) {
                hydro.unfillBoard();
            }
            for (Element elem : water_molecules) {
                elem.unfillBoard();
            }

            oxygens.clear();
            hydrogens.clear();
            water_molecules.clear();
        }
        return true;
    }


    //method to add oxygens
    public boolean add_oxygen(Element o) {

        Element h1, h2;

        h1 = null;
        h2 = null;

        Vector3 v = new Vector3();
        o.atom.transform.getTranslation(v);

        Node free_1 = o.atom.getNode(Oxygen.FREE_VE_1);
        Node free_2 = o.atom.getNode(Oxygen.FREE_VE_2);
        Node free_l2 = o.atom.getNode(Oxygen.LEFT_VE_2);

        int x_1 = Math.round(v.x + free_1.translation.x);
        int x_2 = Math.round(v.x + free_2.translation.x);

        int y_l = Math.round(v.y + free_l2.translation.y);

        int z_1 = Math.round(v.z + free_1.translation.z);
        int z_2 = Math.round(v.z + free_2.translation.z);

        if(y_l >= 6){
            over = true;
        }


        for (Element h : hydrogens) {
            Vector3 hv = new Vector3();
            h.atom.transform.getTranslation(hv);

            int h_x = Math.round(hv.x);
            int h_y = Math.round(hv.y);
            int h_z = Math.round(hv.z);

            if (h_x == x_1 && h_z == z_1 && h_y == y_l) {
                ++stables;
                h.reduceValence();
                o.reduceValence();
                h1 = h;
            } else if (h_x == x_2 && h_x == x_1 - 1 && h_z == z_2 && h_y == y_l) {
                ++stables;
                h.reduceValence();
                o.reduceValence();
                h2 = h;
            }
        }

        if(h1 != null && h2 != null){
            for(Material m : o.atom.materials){
                m.set(ColorAttribute.createDiffuse(new Color(64f/255f,164f/255f,223f/255f,1f)));
            }

            for(Material m : h1.atom.materials){
                m.set(ColorAttribute.createDiffuse(new Color(64f/255f,164f/255f,223f/255f,1f)));
            }

            for(Material m : h2.atom.materials){
                m.set(ColorAttribute.createDiffuse(new Color(64f/255f,164f/255f,223f/255f,1f)));
            }

            water_molecules.add(o);
            water_molecules.add(h1);
            water_molecules.add(h2);
            waters += 4;

            if(water_molecules.size >= 6){
                for(Element oxo : oxygens){
                    oxo.unfillBoard();
                }
                for(Element hydro : hydrogens){
                    hydro.unfillBoard();
                }
                for(Element elem : water_molecules){
                    elem.unfillBoard();
                }

                oxygens.clear();
                hydrogens.clear();
                water_molecules.clear();
            }

            return true;

        }else{
            oxygens.add(o);
            o.fillBoard();
        }

        return false;
    }

    public int get_score(){
        int total = waters + stables;
        return total;
    }

    public void render(){

        if(frame == 0){
            for(Element h : hydrogens){
                if(!h.isStable()) {
                    h.atom.transform.translate(-.1f, 0f, 0f);
                }else{
                    Vector3 v = new Vector3();
                    h.atom.transform.getTranslation(v);
                    h.atom.transform.setToTranslation(Math.round(v.x),v.y,v.z);
                }

                h.renderParts();
            }
            for(Element o : oxygens){
                if(!o.isStable()) {
                    o.atom.transform.translate(-.1f, 0f, 0f);
                }else{
                    Vector3 v = new Vector3();
                    o.atom.transform.getTranslation(v);
                    o.atom.transform.setToTranslation(Math.round(v.x),v.y,v.z);
                }

                o.renderParts();
            }
            frame = 1;
        }
        else{
            if(frame == 1){
                for(Element h : hydrogens){
                    if(!h.isStable()) {
                        h.atom.transform.translate(.1f, 0f, 0f);
                    }else{
                        Vector3 v = new Vector3();
                        h.atom.transform.getTranslation(v);
                        h.atom.transform.setToTranslation(Math.round(v.x),v.y,v.z);
                    }

                    h.renderParts();
                }

                for(Element o : oxygens){
                    if(!o.isStable()) {
                        o.atom.transform.translate(.1f, 0f, 0f);
                    }else{
                        Vector3 v = new Vector3();
                        o.atom.transform.getTranslation(v);
                        o.atom.transform.setToTranslation(Math.round(v.x),v.y,v.z);
                    }

                    o.renderParts();
                }
                frame = 0;
            }
        }

        for(Element e : water_molecules){
            e.renderParts();
        }

    }

}

