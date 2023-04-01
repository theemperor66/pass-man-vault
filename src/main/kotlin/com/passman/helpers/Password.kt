package com.passman.helpers

 public class Password {
     private var id = "";
    private var url = "";
    private var username = "";
    private var annot = "";
    private var pw = "";
    private var owner ="";
    private var roles: MutableList<String> = mutableListOf();
     public fun save_to_db(){
         //To be added - Need more info
     }
     public fun getId(): String {
         return id
     }
     public fun setId(c_Id: String) {
         id = c_Id
     }
     public fun get_url():String{
         return this.url;
     }
     public fun get_username():String{
         return this.username;
     }
     public fun get_annot():String{
         return this.annot;
     }
     public fun get_pw():String{
         return this.pw;
     }
     public fun get_owner():String{
         return this.owner;
     }
     public fun get_roles():List<String>{
         return this.roles;
     }
     public fun set_url(c_url:String){
          this.url = c_url;
     }
     public fun set_username(c_username:String){
         this.username = c_username;
     }
     public fun set_annot(c_annot:String){
         this.annot = c_annot;
     }
     public fun set_pw(c_pw:String){
          this.pw = c_pw;
     }
     public fun set_owner(c_owner:String){
         this.owner = c_owner;
     }
     public fun set_roles(c_roles:List<String>){
          this.roles = ArrayList<String>(c_roles);
     }

     public fun add_role(new_role:String):Boolean{
         roles.add(new_role);
         return true;
     }
     override fun equals(other:Any?):Boolean{
         when(other){
             is Password -> {
                 return this.id == other.id
             }
             else -> return false
         }

     }

}