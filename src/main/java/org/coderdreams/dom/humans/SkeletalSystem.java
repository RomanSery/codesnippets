package org.coderdreams.dom.humans;

import java.util.ArrayList;
import java.util.List;

public interface SkeletalSystem {
    List<Bone> bones = new ArrayList<>();
    List<Joint> joints = new ArrayList<>();

    abstract String getSex();

    default void addBone(Bone b) {
        bones.add(b);
        recalculateJointHealth();
    }

    default void addJoint(Joint j) {
        joints.add(j);
        recalculateJointHealth();
    }

    default int getBoneMass() {
        return bones.stream().map(Bone::getMass).reduce(0, Integer::sum);
    }

    private void recalculateJointHealth() {
        for(Joint j : joints) {
            //... do something
        }
    }

    public class Bone {
        private String name;
        private int mass;

        public Bone(String name, int mass) {
            this.name = name;
            this.mass = mass;
        }

        public int getMass() { return mass; }
        public void setMass(int mass) { this.mass = mass; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public class Joint {
        private Bone b1;
        private Bone b2;
        private int strength;

        public Joint(Bone b1, Bone b2, int strength) {
            this.b1 = b1;
            this.b2 = b2;
            this.strength = strength;
        }

        public Bone getB1() { return b1; }
        public void setB1(Bone b1) { this.b1 = b1; }

        public Bone getB2() { return b2; }
        public void setB2(Bone b2) { this.b2 = b2; }

        public int getStrength() { return strength; }
        public void setStrength(int strength) { this.strength = strength; }
    }
}
