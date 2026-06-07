# Proposal: Fix Encryption Redundancy in ServidorCentral

## Intent

Resolve document (DNI) corruption during serialization/persistence or network transmission. The system currently mutates `Turno` instances in-place when encrypting, causing multiple consecutive encryption rounds and copy-paste bugs during decryption, resulting in unrecoverable data.

## Scope

### In Scope
- Modify `GestorFila` to perform deep copies of `Turno` and related collection structures before applying encryption.
- Maintain in-memory states (`this.fila`, `this.historial`, `this.llamados`) strictly in plain-text DNI format.
- Fix copy-paste bug in `desencriptarHistorial` where it calls `encriptador.encriptar` instead of `desencriptar` on the `turnoActual`.
- Ensure server restarts recover state successfully by starting from a clean initial state (deleting current corrupted files).

### Out of Scope
- Supporting/salvaging previously corrupted double-encrypted files from before the fix.
- Re-architecting the `ISeguridadStrategy` implementation itself.

## Capabilities

### New Capabilities
- None

### Modified Capabilities
- None

## Approach

Implement a cloning/deep-copy utility or method for `Turno` objects. In `GestorFila`, when saving states or publishing events:
1. Deep copy the `Turno` or collection of `Turno`s.
2. Encrypt the copied objects transiently.
3. Save/serialize or publish the transient encrypted copies.
4. Keep the original in-memory entities untouched (plain-text).
Fix `desencriptarHistorial` to call `desencriptar` for `turnoActual`.

## Affected Areas

| Area | Impact | Description |
|------|--------|-------------|
| `ServidorCentral/src/gestorFila/GestorFila.java` | Modified | Update save/load methods to use deep-copy encryption and fix decryption bug. |
| `ServidorCentral/src/eventos/Turno.java` | Modified | Add cloning/copy constructor support. |

## Risks

| Risk | Likelihood | Mitigation |
|------|------------|------------|
| Shallow copies of fields causing shared state mutation | Low | Ensure deep copy handles all mutable fields of `Turno` properly. |

## Rollback Plan

Revert `GestorFila.java` and `Turno.java` to their pre-change state using Git.

## Dependencies

- None

## Success Criteria

- [x] In-memory DNI values remain in plain text.
- [x] Saved persistence files contain single-encrypted DNI values.
- [x] Server restart successfully loads and decrypts persistence files.
