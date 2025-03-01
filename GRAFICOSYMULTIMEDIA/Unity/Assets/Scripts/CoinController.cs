using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CoinController : MonoBehaviour
{
    public float velocidadRotacion = 100f;

    void Update()
    {
        transform.Rotate(0, 0, velocidadRotacion * Time.deltaTime);
    }
}
