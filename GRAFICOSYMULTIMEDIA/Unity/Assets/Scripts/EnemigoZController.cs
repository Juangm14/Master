using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class EnemigoZController : MonoBehaviour
{
    public float speed = 4f; // Velocidad constante del enemigo
    private Rigidbody rb;

    void Start()
    {
        rb = GetComponent<Rigidbody>();
        rb.velocity = new Vector3(0, rb.velocity.y, speed); // Establecer velocidad en Z con la misma velocidad en Y

        // Asegurarse de que no haya desaceleración angular
        rb.angularDrag = 0f; // Desactivar la desaceleración de la rotación
    }

    void Update()
    {
        // Asegurarse de que la velocidad en Z se mantenga constante
        if (Mathf.Abs(rb.velocity.z) < speed)
        {
            rb.velocity = new Vector3(rb.velocity.x, rb.velocity.y, Mathf.Sign(rb.velocity.z) * speed);
        }
    }

    void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.CompareTag("suelo"))
        {
            // Invertir la dirección en el eje Z sin modificar la velocidad
            rb.velocity = new Vector3(rb.velocity.x, rb.velocity.y, -Mathf.Sign(rb.velocity.z) * speed);
        }
    }
}
