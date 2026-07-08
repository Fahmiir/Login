async function register(){

    await fetch(
        "http://localhost:8082/auth/register",
        {

            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            body:JSON.stringify({

                username:document.getElementById("username").value,
                email:document.getElementById("email").value,
                password:document.getElementById("password").value

            })

        });

    alert("Register Success");

}