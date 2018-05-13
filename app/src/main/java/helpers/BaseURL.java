package helpers;

import java.util.ArrayList;
import java.util.List;

import model.AccommodationRoom;
import model.Food;

/**
 * Created by Olivine on 5/5/2017.
 */

public class BaseURL {
//    public static final String BASE_URL="http://192.168.88.244:1000/api/portal/";
//    public static final String IMAGE_BASE_URL ="http://192.168.88.244:1000/upload/";
    public static final String BASE_URL="http://travelbd.net/api/portal/";
    public static final String IMAGE_BASE_URL ="http://travelbd.net/upload/";

    public static final String FOOD_IMAGE_BASE_URL= IMAGE_BASE_URL +"destination_food_service_provider_image/";
    public static final String PACKAGE_IMAGE_BASE_URL= IMAGE_BASE_URL +"package_gallery_image/";
    public static final String PACKAGE_INCLUSION_BASE_URL= IMAGE_BASE_URL +"package_inclusion_image/";
    public static final String HOTEL_IMAGE_BASE_URL= IMAGE_BASE_URL +"accommodation_gallery_image/";

    public static final String HOTEL_ROOM_IMAGE_BASE_URL= IMAGE_BASE_URL +"accommodation_room_gallery_image/";
    //destinations
    public static final String DESTINATION_IMAGE_BASE_URL = IMAGE_BASE_URL + "destination_image/";
    public static final String DESTINATION_IMAGE_GALLERY_BASE_URL = IMAGE_BASE_URL + "destination_gallery_image/";

    public static final String DESTINATION_NEAR_BY_PLACE_IMAGE_BASE_URL = IMAGE_BASE_URL + "destination_nearby_place_image/";
    public static final String DESTINATION_NEAR_BY_PLACE_GALLERY_BASE_URL = IMAGE_BASE_URL+ "destination_nearby_place_gallery_image/";

    public static final String DESTINATION_ATTRACTION_IMAGE_BASE_URL = IMAGE_BASE_URL + "destination_attraction_image/";

    ///language switcher
    public static boolean LANGUAGE_ENG = true;
    public  static Food food = new Food();

    public static String REVIEWED_FOOD_ID = "";
    public static Float REVIEWED_FOOD_RATING=0f;
    public static String REVIEWED_ACCOMMODATION_ID="";
    public static Float REVIEWED_ACCOMMODATION_RATING=0f;
    public static int totalCost = 0;


    public static Float FoodReviewByUser = 0f;


    public static Float REVIEW = 0f;
    public static int  REVIEW_COUNT = 0;

    public static String FRAGMENT_OF_MAIN ="";
    public boolean loginSuccess =false;

    public static ArrayList<AccommodationRoom> accommodationRooms = new ArrayList<>() ;
}
