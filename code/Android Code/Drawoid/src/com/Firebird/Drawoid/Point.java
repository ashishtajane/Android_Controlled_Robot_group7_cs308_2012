package com.Firebird.Drawoid;

public class Point {
    float x, y;  
    
    Point (float a, float b){
        x = a;
        y = b;
    }
    
    //this function calculates the length of line segment obtained by joining this point and point b.
    public float length(Point b){
       return (float) Math.sqrt(Math.pow((this.x - b.x), 2) + Math.pow((this.y - b.y), 2));
   }
    
   //this function calculates the acute angle made by x and y at this point.
   public int angle(Point x, Point y){
       
       int degree;
       float numerator, denominator;
       float l1, l2, l3;
       
       l1 = this.length(x);
       l2 = this.length(y);
       l3 = y.length(x);
       
       numerator = (float) (Math.pow(l1, 2) + Math.pow(l2, 2) - Math.pow(l3, 2));
       denominator = (float) (2 * l1 * l2);
       
       degree = (int) (Math.acos(numerator / denominator) * 180/Math.PI);
       
       return degree;
   }
   
   public String direction(Point a, Point c){
       float t = ((this.x - a.x) * (c.y - this.y)) - ((c.x - this.x) * (this.y - c.y));
       if (t > 0){
           return "L";
       }
       return "R";
   }

   @Override
   public String toString() {
       return x + ", " + y;
   }
}
