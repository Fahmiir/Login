async function resetPassword(){

    const token =
        document.getElementById("token").value;

    const password =
        document.getElementById("password").value;

    const response =
        await fetch(
            "http://localhost:8082/auth/reset-password",
            {

                method:"POST",

                headers:{
                    "Content-Type":"application/json"
                },

                body:JSON.stringify({

                    token:token,
                    newPassword:password

                })

            });

    if(response.ok){

        alert("Password Reset Success");

        window.location.href="login.html";

    }else{

        alert("Reset Failed");

    }

}