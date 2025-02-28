using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EnemyController : MonoBehaviour
{
    public float speed = 4f; // Velocidad constante del enemigo
    private Rigidbody rb;

    void Start()
    {
        rb = GetComponent<Rigidbody>();
        rb.velocity = new Vector3(speed, rb.velocity.y, 0); // Establecer velocidad en X con la misma velocidad en Y

        // Asegurarse de que no haya desaceleración angular
        rb.angularDrag = 0f; // Desactivar la desaceleración de la rotación
    }

    void Update()
    {
        // Asegurarse de que la velocidad en X se mantenga constante
        if (Mathf.Abs(rb.velocity.x) < speed)
        {
            rb.velocity = new Vector3(Mathf.Sign(rb.velocity.x) * speed, rb.velocity.y, rb.velocity.z);
        }
    }

    void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("suelo"))
        {
            // Invertir la dirección en el eje X sin modificar la velocidad
            rb.velocity = new Vector3(-Mathf.Sign(rb.velocity.x) * speed, rb.velocity.y, rb.velocity.z);
        }
    }
}
