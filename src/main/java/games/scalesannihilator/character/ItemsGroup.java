package games.scalesannihilator.character;

import java.util.ArrayList;
import javafx.scene.Group;

public class ItemsGroup extends Group {
   
   private ArrayList<Items> items = new ArrayList<>();
   private ArrayList<Items> removedItems = new ArrayList<>();
   
   public ItemsGroup(){}
   
   public void addItem(Items item){
      items.add(item);
      getChildren().add(item);
   }
   
   public void removeItems(){
      if(!removedItems.isEmpty()){
         for(Items item: removedItems){
            getChildren().remove(item);
         }
         removedItems.clear();
      }
   }
   
   public void addRemoveItem(Items item){
      removedItems.add(item);
   }
   
}
