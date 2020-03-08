package org.coderdreams.dom.humans;

public class Human implements SkeletalSystem {
    private final String sex;
    private int height;
    private int weight;

    public Human(String sex) {
        this.sex = sex;

        Bone lfemur = new Bone("LFemur", 50);
        Bone lHip = new Bone("lHip", 50);
        addBone(lfemur);
        addBone(lHip);

        addJoint(new Joint(lfemur, lHip, 100));

        System.out.println(getBoneMass());
    }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    @Override
    public String getSex() { return sex; }
}
