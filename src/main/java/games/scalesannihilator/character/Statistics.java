package games.scalesannihilator.character;

import javafx.geometry.Point3D;
import lombok.Setter;
import lombok.Getter;

@Getter @Setter
public class Statistics {
   
   public enum Pattern {CIRCLE, TRIANGLE, LINEAR};
   public enum Size {SMALL, BASIC, BIG, BOSS};
   public enum Type {NORMAL, BLOOD, REGENERATE, SPEED, TANK};
   
   private double spawnX = 0;
   private double spawnY = 0;
   private double spawnZ = 0;
   private Point3D zone = new Point3D(0,0,0);
   private double life = 0;
   private double stomach = 0;
   private double energy = 0;
   private double fury = 0;
   private double maxLife = 0;
   private double maxStomach = 0;
   private double maxEnergy = 0;
   private double maxFury = 0;
   private double minDamage = 0;
   private double percentFury = 0;
   private double attack = 0;
   private double defense = 0;
   private double price = 0;
   private double velSwim = 0;
   private double velAtk = 0;
   private Pattern pattern = Pattern.CIRCLE;
   private Size size = Size.BASIC;
   private Type type = Type.NORMAL;

   public Statistics(){}
   
   public Statistics(Statistics other){
      this.life = other.life;
      this.stomach = other.stomach;
      this.energy = other.energy;
      this.fury = other.fury;
      this.maxLife = other.maxLife;
      this.maxStomach = other.maxStomach;
      this.maxEnergy = other.maxEnergy;
      this.maxFury = other.maxFury;
      this.minDamage = other.minDamage;
      this.percentFury = other.percentFury;
      this.attack = other.attack;
      this.defense = other.defense;
      this.price = other.price;
      this.velAtk = other.velAtk;
      this.velSwim = other.velSwim; 
      this.pattern = other.pattern;
      this.size = other.size;
      this.type = other.type;
      this.spawnX = other.spawnX;
      this.spawnY = other.spawnY;
      this.spawnZ = other.spawnZ;
   }
   
   //// Min and Max values
   
   public void absoluteLife(){
      this.life = Math.max(0, Math.min(this.life, maxLife));
   }
   
   public void absoluteFury(){
      this.fury = Math.max(0, Math.min(this.fury, maxFury));
   }
   
   public void absoluteStomach(){
      this.stomach = Math.max(0, Math.min(this.stomach, maxStomach));
   }
    
   public void absoluteEnergy(){
      this.energy = Math.max(0, Math.min(this.energy, maxEnergy));
   }
   
   //// Modifify Dinamic Stats
   public double damageWithFury(Character ch){        
      return Math.max(ch.getStats().minDamage, totalFury(attack) - ch.getStats().defense);
   }
   
   public double damageWithoutFury(Character ch){
      return Math.max(ch.getStats().minDamage, attack - ch.getStats().defense);
   }
   
   public double totalFury(double damage){ 
      return damage * (1 + (fury * percentFury));
   }
   
   public void newLife(double life){
      setLife(getLife() + life);
   }
   
   public void newFury(double fury){
      setFury(getFury() + fury);
   }
   
   public void newStomach(double stomach){      
      setStomach(getStomach() + stomach);
   }
   
   public double percentLife(double maxWidth){
      return life / maxLife * maxWidth;
   }
   
   public double percentFury(double maxWidth){
      return fury / maxFury * maxWidth;
   }
   
}
