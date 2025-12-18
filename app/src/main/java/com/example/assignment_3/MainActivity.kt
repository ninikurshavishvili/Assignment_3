package com.example.assignment_3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_3.adapter.ProductAdapter
import com.example.assignment_3.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productList: MutableList<Product>
    private lateinit var databaseRef: DatabaseReference
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up ActionBar
        supportActionBar?.apply {
            title = "Products"
            setDisplayHomeAsUpEnabled(false)
            elevation = 4f
        }

        setupRecyclerView()
        setupFirebaseListener()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)

        // Set up layout manager with spacing
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Optional: Add item decoration for spacing
        recyclerView.addItemDecoration(
            ItemDecorationVerticalMargin(
                resources.getDimensionPixelSize(R.dimen.item_spacing)
            )
        )

        productList = mutableListOf()

        adapter = ProductAdapter(productList) { product ->
            Toast.makeText(
                this,
                "✅ ${product.name}\n💰 Price: ${product.price}\n📝 ${product.description}",
                Toast.LENGTH_LONG
            ).show()
        }

        recyclerView.adapter = adapter
    }

    private fun setupFirebaseListener() {
        databaseRef = FirebaseDatabase.getInstance().getReference("products")

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.id = productSnapshot.key // Set the Firebase ID
                    product?.let { productList.add(it) }
                }

                // Sort by price or name if needed
                // productList.sortBy { it.price?.toDoubleOrNull() ?: 0.0 }

                adapter.notifyDataSetChanged()

                // Show empty state if no products
                if (productList.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "No products found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "❌ Error: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
