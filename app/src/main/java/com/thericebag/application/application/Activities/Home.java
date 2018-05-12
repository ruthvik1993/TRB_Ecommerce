package com.thericebag.application.application.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.thericebag.application.R;
import com.thericebag.application.application.Dialog.NetworkError_Dialog;
import com.thericebag.application.application.Services.ApiConstants;
import com.thericebag.application.application.Services.NetworkManager;
import com.thericebag.application.application.Services.TheRiceBagRequest;
import com.thericebag.application.application.Services.TheRiceBagResponse;
import com.thericebag.application.application.adapters.HorizontalAdapter;
import com.thericebag.application.application.beans.CategoryNameBean;
import com.thericebag.application.application.beans.FirstPageBean;
import com.thericebag.application.application.beans.ProductBean;
import com.thericebag.application.application.utility.AppConstants;
import com.thericebag.application.application.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home extends Activity {

    DrawerLayout drawerLayout;
    LinearLayout drawer;
//    TextView allProducts;

    ImageView imgKart;
    CustomListAdapter adapter;
    TextView txtShowAllProducts, texteditProfile;

    RelativeLayout relativeOrders, relLayHeader;

    RecyclerView recyclerBestSellers, recyclerAllProducts;

    ImageView imgCall;
    ImageView menuIcon, imgDot1, imgDot2, imgDot3, imgDot4, imgDot5;

    ListView listProducts;

    FirstPageBean firstPageBeanObj;
    ArrayList<ProductBean> bestSellersProductsList = new ArrayList<ProductBean>();
    ArrayList<ProductBean> allProductsList = new ArrayList<ProductBean>();
    //    ArrayList<String> sliderImagesString = new ArrayList<String>();
    String sliderImagesString;
    ArrayList<CategoryNameBean> categoriesList = new ArrayList<CategoryNameBean>();
    ArrayList<CategoryNameBean> emptyList = new ArrayList<>();
    ArrayList<CategoryNameBean> categoriesEmptyList = new ArrayList<CategoryNameBean>();
    ViewPager viewPager;
    //    int[] imageId = {R.drawable.slider_one, R.drawable.slider_two, R.drawable.slider_three, R.drawable.slider_four, R.drawable.slider};
    private ProgressBar rbProprogressBar;
    private TextView txtMyOrders, contactUs;
    private Context mContext = Home.this;
    private RelativeLayout relativeAllproducts;
    private Boolean isListShown = true;

    private TextView textUsername;

    private RelativeLayout relaiveLogout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        Utility.changeStatusbarColor(this, "#54ad54");
        Utility.StartTrackingScreen(Home.this, "1", "Home");

        initializeViews();

//        addDummyData();
//        adapter = new CustomListAdapter(Home.this, categoriesList);
//        listProducts.setAdapter(adapter);

		/*Intent intent = getIntent();
        String firstPageJsonString = intent.getExtras().getString("FirstPageJSON");
		FirstPageBean resultUpdate = new Gson().fromJson(firstPageJsonString, FirstPageBean.class);
		categoriesList = resultUpdate.getAllCategoriesJSONArray();
		bestSellersProductsList = resultUpdate.getBestSellersJSONArray();
		allProductsList = resultUpdate.getAllProductsJSONArray();*/

//		updateBestSellersList(bestSellersProductsList);
//		updateAllProductList(allProductsList);

        addNavigationHeaderView();
        addNavigationFooterView();
        buttonClicks();

        getData();


		/*recyclerBestSellers.setAdapter(new HorizontalAdapter(bestSellersProductsList, new HorizontalAdapter.OnItemClickListener() {
            @Override
			public void onItemClick(ContentItem item) {
				Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_LONG).show();
			}
		}));*/

		/*for (int i = 0; i < 10; i++) {

			View child = View.inflate(Home.this, R.layout.child, null);
			RelativeLayout.LayoutParams childParam = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			child.setLayoutParams(childParam);
			bestSellersLinearLayout.addView(child); //attach to your item
		}*/

		/*for (int i = 0; i < 10; i++) {
            View child = View.inflate(Home.this, R.layout.child, null);
			RelativeLayout.LayoutParams childParam = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			childParam.setMargins(0, 0, 0, 0);
			child.setLayoutParams(childParam);
			linearAllProducts.addView(child); //attach to your item
		}*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /*private void analyticsFunction() {
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();

        mTracker.setScreenName("Image~" + "HomeActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Share")
                .build());
    }*/

    private void setSliderImages() {

        List<String> sliderImagesList = Arrays.asList(sliderImagesString.split(","));

        if (sliderImagesList.size() > 0) {
            PagerAdapter adapter = new CustomImageAdapter(Home.this, sliderImagesList);
            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {
                    if (arg0 == 0) {
                        imgDot1.setImageResource(R.drawable.dot_filled);
                        imgDot2.setImageResource(R.drawable.dot_notfilled);
                        imgDot3.setImageResource(R.drawable.dot_notfilled);
                        imgDot4.setImageResource(R.drawable.dot_notfilled);
                        imgDot5.setImageResource(R.drawable.dot_notfilled);

                    } else if (arg0 == 1) {
                        imgDot1.setImageResource(R.drawable.dot_notfilled);
                        imgDot2.setImageResource(R.drawable.dot_filled);
                        imgDot3.setImageResource(R.drawable.dot_notfilled);
                        imgDot4.setImageResource(R.drawable.dot_notfilled);
                        imgDot5.setImageResource(R.drawable.dot_notfilled);
                    } else if (arg0 == 2) {
                        imgDot1.setImageResource(R.drawable.dot_notfilled);
                        imgDot2.setImageResource(R.drawable.dot_notfilled);
                        imgDot3.setImageResource(R.drawable.dot_filled);
                        imgDot4.setImageResource(R.drawable.dot_notfilled);
                        imgDot5.setImageResource(R.drawable.dot_notfilled);
                    } else if (arg0 == 3) {
                        imgDot1.setImageResource(R.drawable.dot_notfilled);
                        imgDot2.setImageResource(R.drawable.dot_notfilled);
                        imgDot3.setImageResource(R.drawable.dot_notfilled);
                        imgDot4.setImageResource(R.drawable.dot_filled);
                        imgDot5.setImageResource(R.drawable.dot_notfilled);
                    } else if (arg0 == 4) {
                        imgDot1.setImageResource(R.drawable.dot_notfilled);
                        imgDot2.setImageResource(R.drawable.dot_notfilled);
                        imgDot3.setImageResource(R.drawable.dot_notfilled);
                        imgDot4.setImageResource(R.drawable.dot_notfilled);
                        imgDot5.setImageResource(R.drawable.dot_filled);
                    }
                /*if (arg0 != 2) {
                    splashtimer.cancel();
					splashtimer.start();
				} else {
					splashtimer.cancel();
				}*/
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    System.out.print("1. " + arg0 + "\n arg1: " + arg1 + "\n arg2: " + arg2);
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    System.out.print("1. " + arg0);
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String email = Utility.getEmail(Home.this);

        if (email != null) {
//            textUsername.setText(email);
            texteditProfile.setText("Edit Profile");
            relaiveLogout.setVisibility(View.VISIBLE);
            relativeOrders.setVisibility(View.VISIBLE);
        } else {
//            textUsername.setText("Username");
            texteditProfile.setText("Sign In");
            relaiveLogout.setVisibility(View.GONE);
            relativeOrders.setVisibility(View.GONE);
        }

        imgCall.setClickable(true);
    }

    private void addNavigationHeaderView() {
        View headerView = ((LayoutInflater) Home.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.navigation_drawer_header, null, false);
        relativeAllproducts = (RelativeLayout) headerView.findViewById(R.id.relativeAllproducts);
        textUsername = (TextView) headerView.findViewById(R.id.textUsername);
        relLayHeader = (RelativeLayout) headerView.findViewById(R.id.relLayHeader);
        listProducts.addHeaderView(headerView);
    }

    private void getData() {

        TheRiceBagRequest theRiceBagRequest = new TheRiceBagRequest(mContext, TheRiceBagRequest.Method.GET,
                ApiConstants.ApiName.GETFIRSTPAGEJSON, new Response.Listener<TheRiceBagResponse>() {
            @Override
            public void onResponse(TheRiceBagResponse response) {
                rbProprogressBar.setVisibility(View.GONE);    // Start progress bar
                if (response.isSuccess()) {
                    Gson gson = new Gson();
                    firstPageBeanObj = gson.fromJson(response.getResponseBody(), FirstPageBean.class);
                    categoriesList = firstPageBeanObj.getCategories_list();
                    bestSellersProductsList = firstPageBeanObj.getBest_seller_products();
                    allProductsList = firstPageBeanObj.getAllProducts();
                    sliderImagesString = firstPageBeanObj.getSlider_images();

                    HorizontalAdapter bestSellersAdapter = new HorizontalAdapter(Home.this, bestSellersProductsList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Home.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerBestSellers.setLayoutManager(mLayoutManager);
                    recyclerBestSellers.setItemAnimator(new DefaultItemAnimator());
                    recyclerBestSellers.setAdapter(bestSellersAdapter);

                    HorizontalAdapter recyclerAllProductsAdapter = new HorizontalAdapter(Home.this, allProductsList);
                    RecyclerView.LayoutManager mLayoutManagerAllProducts = new LinearLayoutManager(Home.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerAllProducts.setLayoutManager(mLayoutManagerAllProducts);
                    recyclerAllProducts.setItemAnimator(new DefaultItemAnimator());
                    recyclerAllProducts.setAdapter(recyclerAllProductsAdapter);

                    setSliderImages();  // Set Slider Images

                    adapter = new CustomListAdapter(Home.this, categoriesList);
                    listProducts.setAdapter(adapter);
                } else if (response.getErrorMessage().equals(mContext.getResources().getString(R.string.No_Network))) {

                    final NetworkError_Dialog networkError_dialog = new NetworkError_Dialog(mContext);
                    networkError_dialog.showDialog(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            networkError_dialog.dismissDialog();
                            getData();
                        }
                    });

                } else {
                    Toast.makeText(mContext, AppConstants.RESPONSEFAILED, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);

        rbProprogressBar.setVisibility(View.VISIBLE);    // Start progress bar
        NetworkManager.getInstance(mContext).sendRequest(theRiceBagRequest);
    }


    private void addNavigationFooterView() {
        View footerView = ((LayoutInflater) Home.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.navigation_drawer_footer, null, false);
        relativeOrders = (RelativeLayout) footerView.findViewById(R.id.relativeOrders);
        texteditProfile = (TextView) footerView.findViewById(R.id.texteditProfile);
        contactUs = (TextView) footerView.findViewById(R.id.contactUs);
        relaiveLogout = (RelativeLayout) footerView.findViewById(R.id.relaiveLogout);
        listProducts.addFooterView(footerView, "", false);
    }

    private void buttonClicks() {

        contactUs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContactActivity.class);
                startActivity(intent);
            }
        });

        relaiveLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                showConfirmPopUp();
            }
        });

        relLayHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                //editProfile();
            }
        });

        relativeAllproducts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Drawable result = rotate(degree);
//                setCompoundDrawablesWithIntrinsicBounds(result, null, null, null);
                if (isListShown) {
                    ArrayList<CategoryNameBean> emptyList = new ArrayList<CategoryNameBean>();
                    isListShown = false;
                    adapter.relaodList(emptyList);
                } else {
                    isListShown = true;
                    adapter.relaodList(categoriesList);
                }
            }
        });

        relativeOrders.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyOrdersActivity.class);
                startActivity(intent);
            }
        });


        texteditProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        txtShowAllProducts.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawer);
            }
        });

        imgKart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, checkOutActivity.class);
                startActivity(intent);
            }
        });

        imgCall.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                launch();
                //requestforCallPhonePermission();
            }
        });

        //-------------------------Menu----------------------------//
        menuIcon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawer);
            }
        });

        /*edit_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, LoginActivity.class);
                startActivity(intent);
            }
        });*/

        /*allProducts.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });*/

        listProducts.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                position = position - 1;
                categoriesList.get(position).getId();
                categoriesList.get(position).getCat_name();
                Intent intent = new Intent(Home.this, CategorySpecificActivity.class);
                intent.putExtra(AppConstants.CATEGORYNAME, categoriesList.get(position).getCat_name());
                intent.putExtra(AppConstants.ID, categoriesList.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void showConfirmPopUp() {
        final Dialog confirmDialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
        confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        confirmDialog.setContentView(R.layout.dialog_confirm_logout);
        confirmDialog.setCancelable(false);
        confirmDialog.show();

        TextView textIamsure = (TextView) confirmDialog.findViewById(R.id.textIamsure);
        TextView textCancel = (TextView) confirmDialog.findViewById(R.id.textCancel);

        textIamsure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.logout(mContext);
                finish();
                startActivity(getIntent());
                Toast.makeText(mContext, "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });

        textCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
    }

    private void editProfile() {
        String email = Utility.getEmail(mContext);
        if (email != null) {
            Intent intent = new Intent(mContext, EditProfileActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.putExtra(AppConstants.FROM_ACTIVITY, AppConstants.FROM_PROFILE_ACTIVITY);
            startActivity(intent);
        }
    }

    private void initializeViews() {

        imgDot1 = (ImageView) findViewById(R.id.imgDot1);
        imgDot2 = (ImageView) findViewById(R.id.imgDot2);
        imgDot3 = (ImageView) findViewById(R.id.imgDot3);
        imgDot4 = (ImageView) findViewById(R.id.imgDot4);
        imgDot5 = (ImageView) findViewById(R.id.imgDot5);


        rbProprogressBar = (ProgressBar) findViewById(R.id.rbProprogressBar);
        rbProprogressBar.setVisibility(View.GONE);
        txtShowAllProducts = (TextView) findViewById(R.id.txtShowAllProducts);
        listProducts = (ListView) findViewById(R.id.listProducts);
        imgCall = (ImageView) findViewById(R.id.imgCall);
        imgCall.setClickable(true);
        viewPager = (ViewPager) findViewById(R.id.pager);
//        allProducts = (TextView) findViewById(R.id.allProducts);
        imgKart = (ImageView) findViewById(R.id.imgKart);
        menuIcon = (ImageView) findViewById(R.id.menuIcon);
        drawer = (LinearLayout) findViewById(R.id.left_drawer);
        //		aboutLyt = (RelativeLayout) findViewById(R.id.aboustLyt);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerBestSellers = (RecyclerView) findViewById(R.id.recyclerBestSellers);
        recyclerAllProducts = (RecyclerView) findViewById(R.id.recyclerAllProducts);

        //		privacyLyt = (RelativeLayout) findViewById(R.id.privacy);
        //		termsLyt = (RelativeLayout) findViewById(R.id.terms);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int deviceWidth = (int) (displaymetrics.widthPixels / 1.2);
        LayoutParams params = drawer.getLayoutParams();
        params.width = deviceWidth;
        drawer.setLayoutParams(params);
    }

    View insertPhoto(String path) {
        Bitmap bm = decodeSampledBitmapFromUri(path, 220, 220);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LayoutParams(250, 250));
        layout.setGravity(Gravity.CENTER);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LayoutParams(220, 220));
        imageView.setScaleType(ScaleType.CENTER_CROP);
        imageView.setImageBitmap(bm);

        layout.addView(imageView);
        return layout;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        Bitmap bm = null;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    //-----------------------Run Time Permissions-------------------------------------------------//
    private void requestforCallPhonePermission() {
        final String permission = android.Manifest.permission.CALL_PHONE;
        if (ContextCompat.checkSelfPermission(Home.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, permission)) {
                showPermissionRationaleDialog("Please accept this permission to contact us", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(Home.this, new String[]{permission},
                AppConstants.MY_PERMISSIONS_CALL);
    }

    private void launch() {
        imgCall.setClickable(false);
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + AppConstants.PHONE_NUMBER));//change the number
        startActivity(callIntent);
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(Home.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Home.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case AppConstants.MY_PERMISSIONS_CALL:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1
                        && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launch();
                } else {
                    Toast.makeText(Home.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public class CustomListAdapter extends ArrayAdapter<CategoryNameBean> {

        private final Activity context;
        ArrayList<CategoryNameBean> listProducts = new ArrayList<CategoryNameBean>();

        public CustomListAdapter(Activity context, ArrayList<CategoryNameBean> listProducts) {
            super(context, R.layout.list_products, listProducts);
            this.context = context;
            this.listProducts.addAll(listProducts);
            //this.listProducts = listProducts;
        }

        public void relaodList(ArrayList<CategoryNameBean> newlistProducts) {
            listProducts.clear();
            this.listProducts.addAll(newlistProducts);
            notifyDataSetChanged();
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_products, null, true);
            TextView txtProducts = (TextView) rowView.findViewById(R.id.txtProducts);
            if (listProducts.size() > 0) {
                txtProducts.setText(listProducts.get(position).getCat_name());
            }
            return rowView;
        }

        @Override
        public int getCount() {
            return listProducts.size();
        }

        public void updateList(ArrayList<CategoryNameBean> listProducts) {
            this.listProducts = listProducts;
            notifyDataSetChanged();
        }

    }
    //----------------------End Of Run Time Permissions------------------------------------------//


    public class CustomImageAdapter extends PagerAdapter {
        Context context;
        List<String> imagesList = new ArrayList<String>();
        LayoutInflater layoutInflater;

        public CustomImageAdapter(Context context, List<String> imagesList) {
            this.context = context;
            this.imagesList = imagesList;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (imagesList != null) {
                return imagesList.size();
            }
            return 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            View viewItem = inflater.inflate(R.layout.image_item, container, false);
            ImageView imageView = (ImageView) viewItem.findViewById(R.id.imageView);

            imageView.setScaleType(ScaleType.FIT_XY);

            //imageView.setImageResource(imageId[position]);

            Picasso.with(mContext)
                    .load(imagesList.get(position))
                    //.placeholder(R.drawable.progress_animation)
                    .into(imageView);

            container.addView(viewItem);

            return viewItem;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // container.removeView((RelativeLayout) object);
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == ((RelativeLayout) arg1);
            return arg0 == ((View) arg1);
        }
    }
}
