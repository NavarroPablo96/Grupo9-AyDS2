## Exploration: fix-encryption-redundancy

### Current State
In the current implementation, `GestorFila` performs encryption on the `Turno` objects in-place. This mutates the `Documento` (DNI) field of `Turno` instances directly inside the in-memory queue (`this.fila`), history (`this.historial`), and llamados registry (`this.llamados`).
Because the same `Turno` instances are modified, subsequent calls to save or send events encrypt the already-encrypted DNI values multiple times consecutively. Additionally, there is a copy-paste bug in `desencriptarHistorial` where it calls `encriptador.encriptar` instead of `desencriptar` on the `turnoActual`.

### Affected Areas
- `ServidorCentral/src/gestorFila/GestorFila.java` — `encriptarFila`, `encriptarHistorial`, `desencriptarHistorial` (bug at line 116), `encriptarRellamados`, and `LlamarSiguiente` all mutate DNI strings in-place.
- `ServidorCentral/src/gestorFila/ColaTurno.java` — Shallow copy in `generarCopia` and checks in `DniRegistrado` fail once DNI is encrypted in memory.

### Approaches
1. **Deep Copy and Transient Encryption** — Maintain in-memory `Turno` states in plain-text. Perform deep copies of `Turno` objects when serializing for persistence or transmitting over the network, and encrypt only the copies. Decrypt the deserialized entities upon load.
   - Pros:
     - Thread-safe, avoids side-effects on shared memory collections.
     - Prevents redundant/consecutive encryption.
     - Clean separation between in-memory plain-text data and persisted/transmitted encrypted data.
   - Cons:
     - Small memory allocation overhead for transient cloned objects.
   - Effort: Low

2. **In-place Encryption and Rollback Decryption** — Perform in-place encryption, run the persistence/transmission logic, and immediately decrypt the objects back.
   - Pros:
     - No new object allocations.
   - Cons:
     - Prone to race conditions and highly unsafe under concurrent execution.
     - If an exception occurs mid-operation, the queue remains in a corrupted/encrypted state.
   - Effort: Medium

### Recommendation
Approach 1 is recommended. Creating transient cloned objects for persistence/network boundaries is standard practice in Java, eliminating mutations, concurrent access issues, and redundant encryption side-effects. We will also fix the copy-paste bug in `desencriptarHistorial`.

### Risks
- Serialization formats (JSON/XML/TXT) must be verified to ensure the loaded entities are properly decrypted on startup/load.

### Ready for Proposal
Yes — The orchestrator should proceed to define the spec and design for replacing in-place mutation with deep-copy encryption during save/send operations.
