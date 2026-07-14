async function logout(){

    const token =
        localStorage.getItem("token");

    await fetch(
        "http://localhost:8082/api/auth/logout",
        {

            method:"POST",

            headers:{
                Authorization:
                    "Bearer " + token
            }

        });

    localStorage.removeItem("token");

    window.location.href = "login.html";
}