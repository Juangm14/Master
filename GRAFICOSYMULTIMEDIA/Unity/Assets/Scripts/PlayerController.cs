using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class PlayerController : MonoBehaviour
{
    public float velocidad;
    public Text countText;
    public Text winText;
    public Text vidasText;

    private Rigidbody rb;
    private int contador;
    private int contadorMonedas;
    private int contadorVidas;

    public float fuerzaSalto = 6f;
    private bool enSuelo;

    private bool cargandoNivel = false;

    void Start()
    {
        SceneManager.sceneLoaded += OnSceneLoaded; // Registrar el evento de carga de la escena

        rb = GetComponent<Rigidbody>();
        contador = 0;
        contadorMonedas = 0;
        contadorVidas = 2;
        SetCountText();
        winText.text = "";
        UpdateVidasText();
        rb.useGravity = true;
        rb.constraints = RigidbodyConstraints.None;

        Collider playerCollider = GetComponent<Collider>();
        if (playerCollider == null)
        {
            playerCollider = gameObject.AddComponent<SphereCollider>();
        }
    }

    void FixedUpdate()
    {
        float posH = Input.GetAxis("Horizontal");
        float posV = Input.GetAxis("Vertical");

        if (transform.position.y < -10.0f)
        {
            PerderVida();
        }

        Vector3 movimiento = new Vector3(posH, 0.0f, posV);
        rb.AddForce(movimiento * velocidad);

        if (Input.GetKeyDown(KeyCode.Space) && Mathf.Abs(rb.velocity.y) < 0.2f)
        {
            rb.AddForce(Vector3.up * fuerzaSalto, ForceMode.Impulse);
            enSuelo = false;
        }
    }

    void OnTriggerEnter(Collider other)
    {
        if (other.gameObject.CompareTag("mono"))
        {
            other.gameObject.SetActive(false);
            contador++;
            SetCountText();
        }

        if (other.gameObject.CompareTag("coin"))
        {
            other.gameObject.SetActive(false);
            contadorMonedas++;
            SetCountText();
        }

        if (other.gameObject.CompareTag("heart"))
        {
            other.gameObject.SetActive(false);
            contadorVidas++;
            UpdateVidasText();
        }
    }

    void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("enemigo"))
        {
            PerderVida();
        }
    }

    void SetCountText()
    {
        string escenaActual = SceneManager.GetActiveScene().name;

        int requeridosMonos = 0;
        int requeridosMonedas = 0;

        switch (escenaActual)
        {
            case "Nivel1":
                requeridosMonos = 5;
                requeridosMonedas = 0;
                break;
            case "Nivel2":
                requeridosMonos = 4;
                requeridosMonedas = 0;
                break;
            case "Nivel3":
                requeridosMonos = 4;
                requeridosMonedas = 1;
                break;
            case "Nivel4":
                requeridosMonos = 5;
                requeridosMonedas = 2;
                break;
            case "Nivel5":
                requeridosMonos = 5;
                requeridosMonedas = 3;
                break;
        }

        countText.text = "Monos: " + contador.ToString() + "/" + requeridosMonos.ToString() + "\n" +
                        "Monedas: " + contadorMonedas.ToString() + "/" + requeridosMonedas.ToString();

        if (contador >= requeridosMonos && contadorMonedas >= requeridosMonedas)
        {
            winText.text = "Ganaste!!";
            if (!cargandoNivel)
            {
                cargandoNivel = true;
                Invoke("CargarSiguienteNivel", 2f);
            }
        }
    }

    void UpdateVidasText()
    {
        string vidasDisplay = "";
        for (int i = 0; i < contadorVidas; i++)
        {
            vidasDisplay += "❤ ";
        }
        vidasText.text = "Vidas: " + vidasDisplay;
    }

    void PerderVida()
    {
        contadorVidas--;
        UpdateVidasText();
        if (contadorVidas <= 0)
        {
            winText.text = "Perdiste!!";
            Invoke("QuitGame", 1.5f);
        }
        else
        {
            transform.position = new Vector3(-2.25f, 0.5f, 7.69f);
        }
    }

    void CargarSiguienteNivel()
    {
        string escenaActual = SceneManager.GetActiveScene().name;
        string siguienteEscena = "";

        switch (escenaActual)
        {
            case "Nivel1": siguienteEscena = "Nivel2"; break;
            case "Nivel2": siguienteEscena = "Nivel3"; break;
            case "Nivel3": siguienteEscena = "Nivel4"; break;
            case "Nivel4": siguienteEscena = "Nivel5"; break;
            case "Nivel5":
                winText.text = "¡Juego terminado!";
                return;
        }

        if (!string.IsNullOrEmpty(siguienteEscena))
        {
            SceneManager.LoadScene(siguienteEscena);
        }
    }

    void OnSceneLoaded(Scene scene, LoadSceneMode mode)
    {
        cargandoNivel = false;
    }

    void QuitGame()
    {
#if UNITY_EDITOR
        UnityEditor.EditorApplication.isPlaying = false;
#else
        Application.Quit();
#endif
    }
}
