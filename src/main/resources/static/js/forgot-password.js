async function forgotPassword(){

    const email =
        document.getElementById("email").value;

    const response =
        await fetch(
            "http://localhost:8082/auth/forgot-password",
            {

                method:"POST",

                headers:{
                    "Content-Type":"application/json"
                },

                body:JSON.stringify({

                    email:email

                })

            });

    if(response.ok){

        alert("Reset password link sent.");

    }else{

        alert("Failed.");

    }

}