using UnityEngine;
using System.Collections;

public class SueloController : MonoBehaviour {
    private Rigidbody rb;

    // Use this for initialization
    void Start () {
       rb = GetComponent<Rigidbody>();
    }
	
	// Update is called once per frame
	void FixedUpdate () {
        float posH = Input.GetAxis("Horizontal");
        float posV = Input.GetAxis("Vertical");

        Vector3 movimiento = new Vector3(posH, 0.0f, posV);

        transform.Rotate(new Vector3(posV, posH, 0));
    }
}
