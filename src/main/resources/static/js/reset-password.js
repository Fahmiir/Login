async function resetPassword() {

    const params = new URLSearchParams(window.location.search);

    const token = params.get("token");

    const newPassword =
        document.getElementById("newPassword").value;

    const confirmPassword =
        document.getElementById("confirmPassword").value;

    if (newPassword !== confirmPassword) {
        alert("Password dan Confirm Password tidak sama.");
        return;
    }

    const response = await fetch(
        "/api/auth/reset-password",
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            body: JSON.stringify({
                token: token,
                newPassword: newPassword
            })
        });

    if (response.ok) {

        alert("Password berhasil direset.");

        window.location.href = "/api/login.html";

    } else {

        alert("Reset password gagal.");

    }
}