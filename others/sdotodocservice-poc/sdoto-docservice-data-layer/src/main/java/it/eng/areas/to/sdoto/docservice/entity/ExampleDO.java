package it.eng.areas.to.sdoto.docservice.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Table(name = "EXAMPLE")
public class ExampleDO implements Serializable {

   @Id
   @Column(name= "ID")
   private String id;

   @Column(name= "COL1")
   private String col1;
   
   public ExampleDO(){
	   
   }
   
   public String getId(){
	   return this.id;
   }
   
   public String getCol1(){
	   return this.col1;
   }
   
   public void setId(String id){
	   this.id = id;
   }
   
   public void setCol1(String col1){
	   this.col1 = col1;
   }

}