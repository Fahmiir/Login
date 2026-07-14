async function login(){

    const username =
        document.getElementById("username").value;

    const password =
        document.getElementById("password").value;

    const response =
        await fetch("http://localhost:8082/api/auth/login",{

            method:"POST",

            headers:{
                "Content-Type":"application/json"
            },

            body:JSON.stringify({

                username:username,
                password:password

            })

        });

    if(response.ok){

        const data =
            await response.json();

        localStorage.setItem(
            "token",
            data.accessToken
        );

        alert("Login Success");

        window.location.href="profile.html";

    }else{

        alert("Login Failed");

    }

}