using UnityEngine;
using UnityEngine.UI;

public class PlayerController : MonoBehaviour
{
    public float velocidad;
    public Text countText;
    public Text winText;
    public Text vidasText;

    private Rigidbody rb;
    private int contador;
    private int contadorVidas;

    public float fuerzaSalto = 6f; // Define la fuerza con la que el jugador saltará.
    private bool enSuelo; // Variable para saber si el jugador está en el suelo.

    void Start()
    {
        rb = GetComponent<Rigidbody>();
        contador = 0;
        contadorVidas = 3; // Inicializar las vidas del jugador
        SetCountText();
        winText.text = "";
        UpdateVidasText(); // Actualizar la visualización de las vidas con corazones
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
            // Pierde una vida cuando cae fuera de la zona permitida
            PerderVida();
        }

        Vector3 movimiento = new Vector3(posH, 0.0f, posV);
        rb.AddForce(movimiento * velocidad);

        // Detecta si se presiona la barra espaciadora y si el jugador está en el suelo.
        if (Input.GetKeyDown(KeyCode.Space) && Mathf.Abs(rb.velocity.y) < 0.2f)
        {
            rb.AddForce(Vector3.up * fuerzaSalto, ForceMode.Impulse); // Aplica una fuerza hacia arriba para realizar el salto.
            enSuelo = false; // Evita que el jugador haga saltos dobles hasta que vuelva a tocar el suelo.
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
    }

    // Detecta si el jugador toca el suelo.
    void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("enemigo"))
        {
            // Pierde una vida al tocar al enemigo
            PerderVida();
        }
    }

    void SetCountText()
    {
        countText.text = "Contador: " + contador.ToString();
        if (contador >= 5)
        {
            winText.text = "Ganaste!!";
            Invoke("QuitGame", 1.5f);
        }
    }

    void UpdateVidasText()
    {
        // Mostrar las vidas con emoticonos de corazón
        string vidasDisplay = "";
        for (int i = 0; i < contadorVidas; i++)
        {
            vidasDisplay += "❤ "; // Añadir un corazón por cada vida
        }
        vidasText.text = "Vidas: " + vidasDisplay;
    }

    void PerderVida()
    {
        // Restar una vida
        contadorVidas--;
        if (contadorVidas <= 0)
        {
            winText.text = "Perdiste!!";
            Invoke("QuitGame", 1.5f);
        }
        else
        {
            // Reposicionar al jugador en las coordenadas (-2.25, -1.53, 7.69)
            transform.position = new Vector3(-2.25f, 0.5f, 7.69f);
            UpdateVidasText(); // Actualizar la visualización de las vidas
        }
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
