async function loadProfile(){

    const token =
        localStorage.getItem("token");

    const response =
        await fetch(
            "http://localhost:8082/profile",
            {

                headers:{

                    Authorization:
                        "Bearer " + token

                }

            });

    const data =
        await response.json();

    document.getElementById("result")
        .innerHTML=

        JSON.stringify(data);

}

loadProfile();