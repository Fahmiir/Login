async function forgotPassword(){

    const email =
        document.getElementById("email").value;

    const response =
        await fetch(
            "http://localhost:8082/api/auth/forgot-password",
            {

                method:"POST",

                headers:{
                    "Content-Type":"application/json"
                },

                body:JSON.stringify({

                    email:email

                })

            });

    console.log(response.status);

    const text = await response.text();

    console.log(text);

    if(response.ok){

        alert("Reset password link sent.");

    }else{

        alert("Failed.");

    }

}