using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HeartController : MonoBehaviour
{
    public float velocidadRotacion = 100f;

    void Update()
    {
        transform.Rotate(0, velocidadRotacion * Time.deltaTime, 0);
    }
}
