console.log("This is a script file");

const toggleSidebar= () =>{

if($('.sidebar').is(":visible")){

$(".sidebar").css("display","none");
$(".content").css("margin-left","0%");
}
else{
    $(".sidebar").css("display","block");
    $(".content").css("margin-left","20%");
}

};


const search = () => {

    let query = $("#search-input").val();
   
    if (query === '') {
        $(".search-result").hide();

    } else {
        console.log(query);
        
        // Use backticks for string interpolation
        let url = `http://localhost:8080/search/${query}`;
        
        fetch(url)
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            console.log(data);
            let text=`<div class='list-group'>`;
            data.forEach((contact)=>
            {
				text+=`<a href='/user/${contact.cid}/contact' class='list-group-item list-group-item-action'>${contact.firstname}</a>`
			});
			text+=`</div>`;
			$(".search-result").html(text);
			$(".search-result").show();
            // Here you can handle the data, like displaying search results in the UI
        });

        $(".search-result").show();
    }
};

const paymentStart = () => {
    console.log("Payment started...");

    // Define the amount variable and get its value
    let amount = $("#payment_feild").val().trim();
    console.log("Amount entered:", amount);

    // Validate the amount
    if (amount === "" || isNaN(amount) || amount <= 0) {
        alert("Please enter a valid amount greater than zero!");
        return;
    }

    // Use the defined amount in the AJAX request
    $.ajax({
        url: '/user/create_order',
        data: JSON.stringify({ amount: amount, info: 'order_request' }),
        contentType: 'application/json',
        type: 'POST',
        dataType: 'json',
        success: function(response) {
            console.log("Order created successfully:", response);
        },
        error: function(error) {
            console.log("Error creating order:", error);
            alert("Something went wrong");
        }
    });
};
