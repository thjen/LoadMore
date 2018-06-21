package qthjen_dev.io.loadmore;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private List<Contact> contacts;
    private MAdapter contactAdapter;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        contacts = new ArrayList<>();
        random = new Random();

        // create data
        for (int i = 0; i < 5; i++) {
            contacts.add(new Contact("thjenxxxno" + String.valueOf(i) + "@gmail.com", phoneNumberGenerating()));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new MAdapter(recyclerView, contacts, this);
        recyclerView.setAdapter(contactAdapter);

        contactAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (contacts.size() <= 30) {
                    contacts.add(null); // nếu gặp item null thì nó sẽ coi đó là Loading View
                    contactAdapter.notifyItemInserted(contacts.size() - 1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contacts.remove(contacts.size() - 1); // Remove null ờ trên
                            contactAdapter.notifyItemRemoved(contacts.size());

                            //Generating more data
                            int index = contacts.size();
                            int end = index + 5;
                            for (int i = index; i < end; i++) {
                                contacts.add(new Contact("thjenxxxno" + String.valueOf(i) + "@gmail.com", phoneNumberGenerating()));
                            }
                            contactAdapter.notifyDataSetChanged();
                            contactAdapter.setLoaded();
                        }
                    }, 2000);
                } else {
                    Toast.makeText(MainActivity.this, "Loading data completed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String phoneNumberGenerating() {
        int low = 100000000;
        int high = 999999999;
        int randomNumber = random.nextInt(high - low) + low;

        return "0" + randomNumber;
    }
}
