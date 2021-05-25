package com.andeptrai.datn_restaurant_app_manager;

public class URL {

    // check info login restaurant
    public static String urlCheckInfoLoginRestaurant = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/checkInfoLoginRestaurant.php";

    //get all food by id restaurant
    public static String urlGetAllFoodByIdRes = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/getAllFoodByIdRes.php";

    //create new food
    public static String urlCreateNewFood = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/createNewFood.php";

    //edit food
    public static String urlEditFood = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/editFood.php";

    //delete food
    public static String urlDeleteFood = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/deleteFood.php";

    //get all cmt by id restaurant
    public static String urlGetAllCmtByIdRes = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/getAllCmtByIdRes.php";

    //get all notify by id restaurant
    public static String urlGetAllNotifyByIdRes = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/getAllNotifyByIdRes.php";

    //add new notify
    public static String urlCreateNewNotify = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/createNewNotify.php";

    //edit notify
    public static String urlEditNotify = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/editNotify.php";

    //delete notify
    public static String urlDeleteNotify = "http://"+ IP.ip+"/DoAnTotNghiep/androidwebservice/deleteNotify.php";
}
