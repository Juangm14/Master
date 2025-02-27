using UnityEngine;
using UnityEngine.UI;

public class PlayerController : MonoBehaviour
{
    public float velocidad;
    public Text countText;
    public Text winText;

    private Rigidbody rb;
    private int contador;

    public float fuerzaSalto = 5f; // Define la fuerza con la que el jugador saltará.
    private bool enSuelo; // Variable para saber si el jugador está en el suelo.

    void Start()
    {
        rb = GetComponent<Rigidbody>();
        contador = 0;
        SetCountText();
        winText.text = "";
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
            winText.text = "Perdiste!!";
            Invoke("QuitGame", 1.5f);
        }

        Vector3 movimiento = new Vector3(posH, 0.0f, posV);
        rb.AddForce(movimiento * velocidad);

        // Detecta si se presiona la barra espaciadora y si el jugador está en el suelo.
        if (Input.GetKeyDown(KeyCode.Space) && enSuelo) 
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
        enSuelo = true;
    }

    void SetCountText()
    {
        countText.text = "Contador: " + contador.ToString();
        if (contador >= 4)
        {
            winText.text = "Ganaste!!";
            Invoke("QuitGame", 1.5f);
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
